package Lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import StrategyPattern.DiscountStrategy;
import StrategyPattern.PricingContext;
import StrategyPattern.DefaultPricingStrategy;

/**
 * Simple pricing service (Shopei-ei style):
 *  - Loads prices from size.csv and topping.csv once.
 *  - Exposes helpers getSizePrice(), getToppingPrice(), toInt().
 *  - Provides a single calcTotal() used by all Addon panels.
 *  - DiscountStrategy is optional; Default = no discount.
 */
public class PricingService {

    private final Map<String, Double> sizePrice = new HashMap<>();
    private final Map<String, Double> toppingPrice = new HashMap<>();

    public PricingService(String sizeCsvPath, String toppingCsvPath) {
        loadSizeCsv(sizeCsvPath);
        loadToppingCsv(toppingCsvPath);
    }

    private void loadSizeCsv(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (header) { header = false; continue; }
                String[] p = line.split(",");
                if (p.length < 2) continue;
                String code = p[0].trim();
                double price = Double.parseDouble(p[1].trim());
                sizePrice.put(code, price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadToppingCsv(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (header) { header = false; continue; }
                String[] p = line.split(",");
                if (p.length < 2) continue;
                String code = p[0].trim();
                double price = Double.parseDouble(p[1].trim());
                toppingPrice.put(code, price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Helpers =====
    public double getSizePrice(String sizeCode) {
        if (sizeCode == null) return 0.0;
        return sizePrice.getOrDefault(sizeCode, 0.0);
    }

    public double getToppingPrice(String toppingCode) {
        if (toppingCode == null) return 0.0;
        return toppingPrice.getOrDefault(toppingCode, 0.0);
    }

    public int toInt(double v) { return (int)Math.round(v); }

    /**
     * Core calculator used by all Addon panels.
     * @param base          base drink price (hot/iced)
     * @param sizeCode      size code (S001/S002/S003) or null if not used
     * @param extraShots    extra espresso shots (coffee only)
     * @param shotPrice     price per shot (0 for non‑coffee)
     * @param toppingCodes  list of topping codes (may be empty)
     * @param strategy      optional discount strategy (may be null)
     * @param ctx           optional pricing context (may be null)
     * @return total after discount (rounded to int)
     */
    public int calcTotal(
            double base,
            String sizeCode,
            int extraShots,
            double shotPrice,
            List<String> toppingCodes,
            DiscountStrategy strategy,
            PricingContext ctx
    ) {
        double size = getSizePrice(sizeCode);
        double shots = Math.max(0, extraShots) * Math.max(0.0, shotPrice);
        double tops = 0.0;
        if (toppingCodes != null) {
            for (String code : toppingCodes) {
                tops += getToppingPrice(code);
            }
        }
        double subtotal = base + size + shots + tops;
        DiscountStrategy st = (strategy != null) ? strategy : new DefaultPricingStrategy();
        double after = st.apply(subtotal, ctx);
        return toInt(after);
    }

    // Convenience wrappers
    public int calcCoffeeTotal(double base, int extraShots, double shotPrice, List<String> toppingCodes, DiscountStrategy strategy, PricingContext ctx) {
        return calcTotal(base, null, extraShots, shotPrice, toppingCodes, strategy, ctx);
    }

    public int calcTeaTotal(double base, String sizeCode, List<String> toppingCodes, DiscountStrategy strategy, PricingContext ctx) {
        return calcTotal(base, sizeCode, 0, 0.0, toppingCodes, strategy, ctx);
    }
}
