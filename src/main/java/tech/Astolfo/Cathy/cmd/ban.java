package tech.Astolfo.Cathy.cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

import java.util.List;
import java.util.Objects;

public class ban extends Command {

    public ban() {
        super.name = "ban";
        super.aliases = new String[]{"b","ban"};
        super.arguments = "<@user> <reason>";
        super.help = "Ban a user from the Discord";
        super.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        super.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent e) {
        String[] args = e.getArgs().split("\\s+");
        String reason = e.getArgs().replace(args[0]+" ", "");

        if (args.length < 2) {
            e.replyError("**Invalid arguments!** Syntax: *"+e.getClient().getPrefix()+"ban <@user> <reason>*");
            return;
        }

        List<User> users = e.getMessage().getMentionedUsers();
        if (users.size() != 1) {
            e.replyError("**Invalid mention count!** Syntax: *"+e.getClient().getPrefix()+"ban <@user> <reason>*");
            return;
        }

        User u = users.get(0);

        if (!e.getMember().canInteract(Objects.requireNonNull(e.getGuild().getMember(u)))) {
            e.replyError("**Hey!** You you don't have permission to ban that user");
            return;
        } else if (Objects.requireNonNull(e.getGuild().getMember(e.getSelfUser())).canInteract(Objects.requireNonNull(e.getGuild().getMember(u)))) {
            e.replyError("**Sorry!** I don't have permission to ban that user");
            return;
        }

        MessageEmbed ban_msg = new EmbedBuilder()
                .setAuthor("Team Astolfo", "https://astolfo.tech", e.getAuthor().getAvatarUrl())
                .setTitle("You've been banned!")
                .setThumbnail(u.getAvatarUrl())
                .setDescription("Hey "+u.getAsMention()+"!\nYou've been **banned** from the **Team Astolfo** Discord server.\nWe're so sorry to see you go! <:astolfoCry:763502101359099914>\n\nYou were banned by **"+e.getAuthor().getAsTag()+"** for:\n\""+reason+"\"")
                .setFooter("Team Astolfo", e.getGuild().getIconUrl())
                .setTimestamp(Instant.ofEpochMilli(System.currentTimeMillis()))
                .build();

        MessageEmbed log = new EmbedBuilder()
                .setAuthor(e.getAuthor().getAsTag(), null, e.getAuthor().getAvatarUrl())
                .setThumbnail(u.getAvatarUrl())
                .setTitle("Banned "+u.getAsTag()+" <a:ESLint:763176005513904138>")
                .setDescription("ID: "+u.getId()+"\nReason: \""+reason+"\"")
                .build();

        u.openPrivateChannel().queue((channel) -> channel.sendMessage(ban_msg).queue(
                m -> {
                    e.getGuild().ban(Objects.requireNonNull(e.getGuild().getMember(u)), 1).queue();
                    Objects.requireNonNull(e.getGuild().getTextChannelById("766082022141067285")).sendMessage(log).queue();
                    e.reply("Banned user, **"+u.getAsTag()+"**, from the server!)");
                }
        ));
    }
}
