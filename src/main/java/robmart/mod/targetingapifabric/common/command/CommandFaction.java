package robmart.mod.targetingapifabric.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;
import robmart.mod.targetingapifabric.api.reference.Reference;

import java.util.Collection;
import java.util.Iterator;

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
                        .executes((ctx) -> addToFaction(ctx.getSource(), StringArgumentType.getString(ctx, "faction"),
                            ctx.getSource().getPlayerOrThrow()))
                        .then(CommandManager.argument("entities", EntityArgumentType.entities())
                            .executes((ctx) -> addToFaction(ctx.getSource(), StringArgumentType.getString(ctx, "faction"),
                                EntityArgumentType.getEntities(ctx, "entities")))))
                ).then(CommandManager.literal("get")
                    .then(CommandManager.argument("player", EntityArgumentType.players())
                        .executes((context -> getFactions(context.getSource(), (PlayerEntity) EntityArgumentType.getEntity(context, "player")))))
                ).then(CommandManager.literal("list")
                    .then(CommandManager.argument("faction", StringArgumentType.string())
                        .suggests((ctx, builder) -> {
                            Targeting.getFactionList().forEach((faction -> builder.suggest(faction.getName())));
                            return builder.buildFuture();
                        }).executes(ctx -> listFactionMembers(ctx.getSource(), StringArgumentType.getString(ctx, "faction"))))
                    )

        );
    }

    private static int getFactions(ServerCommandSource source, PlayerEntity entity) {
        source.sendFeedback(MutableText.of(new LiteralTextContent(Targeting.getFactionsFromEntity(entity).toString())), true);
        return 1;
    }

    private static int addToFaction(ServerCommandSource source, String faction, PlayerEntity entity) throws CommandSyntaxException {
        Faction iFaction = Targeting.getFaction(faction);
        iFaction.addMemberEntity(entity);

        source.sendFeedback(Text.translatable("commands.kill.success.single", entity.getDisplayName()), true);

        return 1;
    }

    private static int addToFaction(ServerCommandSource source, String faction, Collection<? extends Entity> entities) throws CommandSyntaxException {
        Faction iFaction = Targeting.getFaction(faction);

        for (Entity entity : entities) {
            iFaction.addMemberEntity(entity);
        }

        if (entities.size() == 1) {
            source.sendFeedback(Text.translatable("commands." + Reference.MOD_ID + ".faction.success.single", entities.iterator().next().getDisplayName()), true);
        } else {
            source.sendFeedback(Text.translatable("commands." + Reference.MOD_ID + ".faction.success.multiple", entities.size()), true);
        }

        return 1;
    }

    private static int listFactionMembers(ServerCommandSource source, String faction) {
        Faction iFaction = Targeting.getFaction(faction);
        source.sendFeedback(MutableText.of(new LiteralTextContent(iFaction.getAllMembers().toString())), true);
        return 1;
    }
}
