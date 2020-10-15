package tech.Astolfo.Cathy.cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class purge extends Command {

    public purge() {
        super.name = "purge";
        super.aliases = new String[]{"clear","rm"};
        super.help = "Delete x amount of messages from the channel";
        super.arguments = "<amount>";
        super.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        super.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    @Override
    protected void execute(CommandEvent e) {
        String[] args = e.getArgs().split("\\s+");
        int amount = 5;

        try {
            if (args.length < 1) return;
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {}

        if (amount > 250) {
            e.reply("I cannot delete more than **250 messages** at a time!\nDefaulting to 250 messages.");
            amount = 250;
        }

        int finalAmount = amount;
        MessageEmbed log = new EmbedBuilder()
                .setAuthor(e.getAuthor().getAsTag(), null, e.getAuthor().getAvatarUrl())
                .setThumbnail(e.getAuthor().getAvatarUrl())
                .setTitle("Removed "+amount+" messages <a:mycode:763176004372922408>   ")
                .setDescription("Channel: <#"+e.getChannel().getId()+">")
                .build();

        e.getMessage().delete().queue();

        e.getChannel().getHistoryBefore(e.getMessage(), amount).queue(
                res -> {
                    res.getRetrievedHistory().forEach(
                            msg -> msg.delete().queue()
                    );

                    Objects.requireNonNull(e.getGuild().getTextChannelById("766082022141067285")).sendMessage(log).queue();

                    e.getChannel().sendMessage("Successfully deleted **"+ finalAmount +" messages** from this channel!").queue(
                                message -> {
                                        Thread msg_delay = new Thread(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Thread.sleep(5000);
                                                        } catch (InterruptedException interruptedException) {
                                                            interruptedException.printStackTrace();
                                                        }
                                                        message.delete().queue();
                                                }
                                            }
                                        );

                                        msg_delay.start();
                                }
                            );
                }
        );
    }
}
