package clubdev.economyshop.forms;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import clubdev.economyshop.Main;
import clubdev.economyshop.shop.ShopCategory;
import clubdev.economyshop.shop.ShopItem;
import clubdev.economyshop.utils.Util;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.ModalForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.StepSlider;
import java.util.Set;

import com.clubdev.economy.utils.Utils;

public class Forms {

    private Main main;

    public Forms(Main main) {
        this.main = main;
    }
    
    public void sendShopForm(Player player) {
        SimpleForm form = new SimpleForm("Магазин");
        form.addContent(Main.PREFIX + "Выберите категорию товаров:");
        main.getCachedCategory().forEach((category, items) -> {
            form.addButton(category.getName(), (pl, b) -> sendCategoryForm(player, category, items));
        });
        form.send(player);
    }

    public void sendCategoryForm(Player player, ShopCategory category, Set<ShopItem> items) {
        SimpleForm form = new SimpleForm(category.getName());
        form.addContent(Main.PREFIX + "Выберите товар который хотите приобрести:");
        items.forEach(item -> {
            form.addButton(item.getName() + "\n§b" + item.getPrice() + "§7/шт.", (pl, b) -> {
                if (!item.getItem().isTool() || !item.getItem().isArmor()) {
                    this.sendSelectCountForm(player, item);
                } else {
                    this.sendConfirmForm(player, item.getPrice(), item.getItem(), item);
                }
            });
        });
        form.send(player);
    }

    public void sendSelectCountForm(Player player, ShopItem item) {
        CustomForm form = new CustomForm("Выберите количество");
        form.addElement("Выберите количество §b" + item.getName() + "§r, которое хотите приобрести.\n\n")
            .addElement("count", new StepSlider("Выберите количество", Util.count(), 1));

        form.setHandler((pl, response) -> {
            int count = response.getStepSlider("count").getSelectedIndex();

            double finalPrice = item.getPrice() * count;
            Item finalItem = Item.get(item.getItem().getId(), item.getItem().getDamage(), count);

            if (main.getEconomyManager().getMoney(player) >= finalPrice) {
                this.sendConfirmForm(player, finalPrice, finalItem, item);
            } else {
                pl.sendMessage("§cУ вас недостаточно денег.");
            }
        });
        form.send(player);
    }

    public void sendConfirmForm(Player player, double finalPrice, Item finalItem, ShopItem shopItem) {
        ModalForm form = new ModalForm("Подтвердите покупку");

        form.addContent(
                "§7Подтвердите свою покупку:\n" + 
                "§fВы покупаете: §6" + shopItem.getName() + " §fв количестве §6" + finalItem.getCount() + "§f.\n\n" +
                "§fЦена: §b" + Utils.getNormalStringMoney(finalPrice) + "§f$§7/шт."
            )
            .setPositiveButton("Подтвердить")
            .setNegativeButton("Назад");

        form.setHandler((pl, response) -> {
            if (response) {
                if (pl.getInventory().canAddItem(finalItem)) {
                    if (main.getEconomyManager().getMoney(player) >= finalPrice) {
                        main.getEconomyManager().reduceMoney(player, finalPrice);
                        pl.getInventory().addItem(finalItem);
                        if (finalItem.getCount() == 1) {
                            pl.sendMessage(Main.PREFIX + "§fВы приобрели §6" + shopItem.getName() + "§f за §b" + Utils.getNormalStringMoney(finalPrice) + "§b$");
                        } else {
                            pl.sendMessage(Main.PREFIX + "§fВы приобрели §6" + shopItem.getName() + "§f в количестве " + finalItem.getCount() + " шт. за §b" + Utils.getNormalStringMoney(finalPrice) + "§b$");
                        }
                    } else {
                        pl.sendMessage("§cУ вас недостаточно денег.");
                    }
                } else {
                    pl.sendMessage("У вас недостаточно места в инвентаре.");
                }
            } else {
                this.sendShopForm(player);
            }
        });
        form.send(player);
    }
}
