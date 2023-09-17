package robmart.mod.targetingapifabric.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
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
                            Targeting.getFactionList().forEach((faction -> builder.suggest(faction.getName())));
                            return builder.buildFuture();
                        })
                        .executes((ctx) -> addToFaction(StringArgumentType.getString(ctx, "faction"),
                            ctx.getSource().getPlayerOrThrow()))
                        .then(CommandManager.argument("entity", EntityArgumentType.players())
                            .executes((ctx) -> addToFaction(StringArgumentType.getString(ctx, "faction"),
                                (PlayerEntity) EntityArgumentType.getEntity(ctx, "entity")))))
                ).then(CommandManager.literal("get")
                    .then(CommandManager.argument("player", EntityArgumentType.players())
                        .executes((context -> getFactions(context.getSource(), (PlayerEntity) EntityArgumentType.getEntity(context, "player")))))
                ).then(CommandManager.literal("list")
                    .then(CommandManager.argument("faction", StringArgumentType.string())
                        .suggests((ctx, builder) -> {
                            Targeting.getFactionList().forEach((faction -> builder.suggest(faction.getName())));
                            return builder.buildFuture();
                        }).executes(ctx -> listFactionMembers(ctx.getSource(), StringArgumentType.getString(ctx, "faction")))))

        );
    }

    private static int getFactions(ServerCommandSource source, PlayerEntity entity) {
        source.sendFeedback(MutableText.of(new LiteralTextContent(Targeting.getFactionsFromEntity(entity).toString())), true);
        return 1;
    }

    private static int addToFaction(String faction, PlayerEntity entity) throws CommandSyntaxException {
        Faction iFaction = Targeting.getFaction(faction);
        iFaction.addMemberEntity(entity);
        return 1;
    }

    private static int listFactionMembers(ServerCommandSource source, String faction) {
        Faction iFaction = Targeting.getFaction(faction);
        source.sendFeedback(MutableText.of(new LiteralTextContent(iFaction.getAllMembers().toString())), true);
        return 1;
    }
}
