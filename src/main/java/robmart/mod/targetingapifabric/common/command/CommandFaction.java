package robmart.mod.targetingapifabric.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;

public class CommandFaction {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("factions")
                        .requires(s -> s.hasPermissionLevel(3))
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("faction", StringArgumentType.string())
                                        .suggests((ctx, builder) -> {
                                            Targeting.getFactionMap().keySet().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .then(CommandManager.argument("player", EntityArgumentType.players())
                                                .executes((ctx) -> addToFaction(StringArgumentType.getString(ctx, "faction"),
                                                        (PlayerEntity) EntityArgumentType.getEntity(ctx, "player")))))
                        ).then(CommandManager.literal("get")
                        .then(CommandManager.argument("player", EntityArgumentType.players())
                                .executes((context -> listFactions(context.getSource(), (PlayerEntity) EntityArgumentType.getEntity(context, "player")))))
                )

        );
    }

    private static int listFactions(ServerCommandSource  source, PlayerEntity entity) {
        source.sendFeedback(new LiteralText(Targeting.getFactionsFromEntity(entity).toString()), true);
        return 1;
    }

    private static int addToFaction(String faction, PlayerEntity entity) throws CommandSyntaxException {
        Faction iFaction = Targeting.getFaction(faction);
        iFaction.addMemberEntity(entity);
        return 1;
    }
}
