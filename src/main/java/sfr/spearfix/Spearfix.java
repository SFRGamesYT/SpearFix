package sfr.spearfix;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

import java.util.ArrayList;

public class Spearfix implements ModInitializer {
    private static final String CHECKED_TAG = "stupid_spear_bozo";
    private static final ArrayList<LivingEntity> bozosMaybe = new ArrayList<LivingEntity>();
    @Override
    public void onInitialize() {
        System.out.println("spear fix started :bru:");
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof LivingEntity living)) return;
            if (!(living instanceof ZombieEntity || living instanceof AbstractPiglinEntity)) return;
            if (!living.addCommandTag(CHECKED_TAG)) return;
            bozosMaybe.add(living);
        });

        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for(LivingEntity living : bozosMaybe){
                if(living.isRemoved()) continue;
                if (living.getEquippedStack(EquipmentSlot.MAINHAND).isIn(ItemTags.SPEARS)) {
                    living.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
            }
            bozosMaybe.clear();
        });
    }
}
