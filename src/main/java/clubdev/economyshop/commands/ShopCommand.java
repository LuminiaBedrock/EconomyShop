package clubdev.economyshop.commands;

import clubdev.economyshop.Main;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ShopCommand extends Command {

    private Main main;

    public ShopCommand(Main main) {
        super("shop", "Магазин");
        this.main = main;
    }
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            main.getForms().sendShopForm(player);
            return true;
        }
        return false;
    }
}
