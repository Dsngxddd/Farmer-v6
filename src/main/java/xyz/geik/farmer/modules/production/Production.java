package xyz.geik.farmer.modules.production;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.production.handlers.ProductionCalculateEvent;
import xyz.geik.farmer.shades.storage.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Production module main class
 */
@Getter
public class Production extends FarmerModule {

    /**
     * Constructor of class
     */
    public Production() {}

    @Getter
    private static Production instance;

    private Config langFile;

    private static ProductionCalculateEvent productionCalculateEvent;

    private final String[] numberFormat = new String[]{"k", "m", "b", "t"};

    private long reCalculate = 15L;

    private final List<String> productionItems = new ArrayList<>();

    /**
     * onEnable method of module
     */
    @Override
    public void onEnable() {
        instance = this;

        if (Main.getConfigFile().getProduction().isStatus()) {
            this.setEnabled(true);
            List<String> productionItems = new ArrayList<>(Main.getConfigFile().getProduction().getItems());
            productionItems.replaceAll(String::toUpperCase);
            getProductionItems().addAll(productionItems);
            productionCalculateEvent = new ProductionCalculateEvent();
            Bukkit.getPluginManager().registerEvents(productionCalculateEvent, Main.getInstance());
            setLang(Main.getConfigFile().getSettings().getLang(), this.getClass());
            numberFormat[0] = getLang().getText("numberFormat.thousand");
            numberFormat[1] = getLang().getText("numberFormat.million");
            numberFormat[2] = getLang().getText("numberFormat.billion");
            numberFormat[3] = getLang().getText("numberFormat.trillion");
            reCalculate = Main.getConfigFile().getProduction().getReCalculate();
        }
    }

    /**
     * onReload method of module
     */
    @Override
    public void onReload() {
        if (!this.isEnabled())
            return;
        numberFormat[0] = getLang().getText("numberFormat.thousand");
        numberFormat[1] = getLang().getText("numberFormat.million");
        numberFormat[2] = getLang().getText("numberFormat.billion");
        numberFormat[3] = getLang().getText("numberFormat.trillion");
        reCalculate = Main.getConfigFile().getProduction().getReCalculate();
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(productionCalculateEvent);
    }

    /**
     * is item suitable to calculate
     *
     * @param item item of farmer
     * @return boolean
     */
    public static boolean isCalculateItem(@NotNull FarmerItem item) {
        return instance.getProductionItems().contains(item.getName())
                || instance.getProductionItems().isEmpty();
    }

}
