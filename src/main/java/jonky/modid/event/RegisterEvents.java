package jonky.modid.event;

import jonky.modid.event.heavycore.AxeUsageEvent;
import jonky.modid.event.heavycore.HoeUsageEvent;
import jonky.modid.event.heavycore.PickaxeUsageEvent;
import jonky.modid.event.heavycore.ShovelUsageEvent;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class RegisterEvents {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(new PickaxeUsageEvent());
        PlayerBlockBreakEvents.BEFORE.register(new AxeUsageEvent());
        PlayerBlockBreakEvents.BEFORE.register(new ShovelUsageEvent());
        PlayerBlockBreakEvents.BEFORE.register(new HoeUsageEvent());
        ShieldEvents.registerShieldEvents();
    }
}
