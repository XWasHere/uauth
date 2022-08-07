package me.xwashere.uauth.mixin;

import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SplashManager.class)
abstract public class SplashManagerMixin {
    @Shadow @Final private List<String> splashes;

    @Inject(method = "Lnet/minecraft/client/resources/SplashManager;apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("TAIL"))
    void apply(List<String> p_118878_, ResourceManager p_118879_, ProfilerFiller p_118880_, CallbackInfo ci) {
        // splashes.add("1984 was warning, not a fucking instruction manual");
        // splashes.clear();

        splashes.add("Also try No Chat Reports!");                               // S: XWasHere
        splashes.add("#saveminecraft");                                          // S: XWasHere
        splashes.add("\"Just don't be toxic\"");                                 // S: XWasHere
        splashes.add("\"Nothing you like about Minecraft is going to change\""); // S: XWasHere
        splashes.add("2 PIXELS");                                                // S: XWasHere
        splashes.add("1 in 7.5 trillion");                                       // S: XWasHere
        splashes.add("The real Minecraft.");                                     // S: XWasHere
        splashes.add("Was that The Bite of '87?!");                              // S: Kamigen
        splashes.add("But hey, capes!!!!1!!1!!!1!");                             // S: Tycrek
        splashes.add("MC-254467");                                               // S: XWasHere
        splashes.add("\"Concept art does not equal promise\"");                  // S:
        splashes.add("Who was in paris?");                                       // S: i-spin
        splashes.add("Any coords to the farm?");                                 // S: MellDa1024 (GH)
        splashes.add("Ungriefed since 84");                                      // S: Kamigen

        // he didn't deserve this.
        splashes.add("The Work of Notch!");                                      // S: Notch
        splashes.add("Made by Notch!");                                          // S: Notch
    }
}
