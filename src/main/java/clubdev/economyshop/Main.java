package clubdev.economyshop;

import clubdev.economyshop.commands.ShopCommand;
import clubdev.economyshop.forms.Forms;
import clubdev.economyshop.shop.ShopCategory;
import clubdev.economyshop.shop.ShopItem;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import lombok.Getter;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import com.clubdev.economy.Economy;
import com.clubdev.economy.managers.EconomyManager;

@Getter
public class Main extends PluginBase {

    public static String PREFIX = "§l§6>§r ";

    private Config shopConfig;
    private Forms forms;

    private EconomyManager economyManager;

    public LinkedHashMap<ShopCategory, Set<ShopItem>> cachedCategory = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        this.saveResource("items.yml");
        this.shopConfig = new Config(new File(this.getDataFolder() + "/items.yml"), Config.YAML);
        this.forms = new Forms(this);

        this.economyManager = Economy.getInstance().getEconomyManager();

        this.loadItems();
        this.getServer().getCommandMap().register("Shop", new ShopCommand(this));
    }

    private void loadItems() {
        shopConfig.getRootSection().forEach((s, o) -> {
            ShopCategory category = new ShopCategory(s, shopConfig.getString(s + ".name"));
            Set<ShopItem> items = new HashSet<>();
            cachedCategory.put(category, items);

            shopConfig.getSection(s + ".items").forEach((key, item) -> {
                String section = s + ".items." + key;
                ShopItem shopItem = new ShopItem(
                    shopConfig.getString(section + ".name"), 
                    Item.get(
                        Integer.parseInt(shopConfig.getString(section + ".id").split(":")[0]),
                        Integer.parseInt(shopConfig.getString(section + ".id").split(":")[1])
                    ), 
                    shopConfig.getDouble(section + ".price")
                );
                items.add(shopItem);
            });
        });
    }
}
