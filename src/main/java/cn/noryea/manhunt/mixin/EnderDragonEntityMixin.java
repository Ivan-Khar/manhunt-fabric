package cn.noryea.manhunt.mixin;

import cn.noryea.manhunt.ManhuntConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragon.class)
public abstract class EnderDragonEntityMixin {

  //End the game when runners kill the enderdragon
  @Inject(method = "tickDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
  private void runnersWon(CallbackInfo ci) {
    try {
      EnderDragon dragon = ((EnderDragon) (Object) this);
      MinecraftServer server = dragon.getServer();
      if (ManhuntConfig.INSTANCE.isRunnersWinOnDragonDeath() && !server.getScoreboard().getPlayerTeam("runners").getPlayers().isEmpty() && dragon.dragonDeathTime == 1) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput().withPermission(2), "title @a subtitle {\"translate\":\"manhunt.win.runners.subtitle\",\"color\":\"white\"}");
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput().withPermission(2), "title @a title {\"translate\":\"manhunt.win.runners.title\",\"color\":\"white\"}");
      }
    } catch (NullPointerException ignored) {}
  }
}
