package clubdev.economyshop.shop;

import cn.nukkit.item.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ShopItem {
    
    private final String name;
    private final Item item;
    private final Double price;
}
