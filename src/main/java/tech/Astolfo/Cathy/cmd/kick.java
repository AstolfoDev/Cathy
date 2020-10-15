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

public class kick extends Command {

    public kick() {
        super.name = "kick";
        super.aliases = new String[]{"k","softban"};
        super.arguments = "<@user> <reason>";
        super.help = "Kick a user who's breaking the rules";
        super.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        super.botPermissions = new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent e) {
        String[] args = e.getArgs().split("\\s+");
        String reason = e.getArgs().replace(args[0]+" ", "");

        if (args.length < 2) {
            e.replyError("**Invalid arguments!** Syntax: *"+e.getClient().getPrefix()+"kick <@user> <reason>*");
            return;
        }

        List<User> users = e.getMessage().getMentionedUsers();
        if (users.size() != 1) {
            e.replyError("**Invalid mention count!** Syntax: *"+e.getClient().getPrefix()+"kick <@user> <reason>*");
            return;
        }

        User u = users.get(0);

        if (!e.getMember().canInteract(Objects.requireNonNull(e.getGuild().getMember(u)))) {
            e.replyError("**Hey!** You you don't have permission to kick that user");
            return;
        } else if (Objects.requireNonNull(e.getGuild().getMember(e.getSelfUser())).canInteract(Objects.requireNonNull(e.getGuild().getMember(u)))) {
            e.replyError("**Sorry!** I don't have permission to kick that user");
            return;
        }

        MessageEmbed kick_msg = new EmbedBuilder()
                .setAuthor("Team Astolfo", "https://astolfo.tech", e.getAuthor().getAvatarUrl())
                .setTitle("You've been kicked!")
                .setThumbnail(u.getAvatarUrl())
                .setDescription("Hey "+u.getAsMention()+"!\nYou've been **kicked** from the **Team Astolfo** Discord server.\nWe're so sorry to see you go! <:astolfoCry:763502101359099914>\n\nYou were kicked by **"+e.getAuthor().getAsTag()+"** for:\n\""+reason+"\"")
                .setFooter("Team Astolfo", e.getGuild().getIconUrl())
                .setTimestamp(Instant.ofEpochMilli(System.currentTimeMillis()))
                .build();

        MessageEmbed log = new EmbedBuilder()
                .setAuthor(e.getAuthor().getAsTag(), null, e.getAuthor().getAvatarUrl())
                .setThumbnail(u.getAvatarUrl())
                .setTitle("Kicked "+u.getAsTag()+" <a:chikachika:763176005241536554>  ")
                .setDescription("ID: "+u.getId()+"\nReason: \""+reason+"\"")
                .build();

        u.openPrivateChannel().queue((channel) -> channel.sendMessage(kick_msg).queue(
                m -> {
                    e.getGuild().kick(Objects.requireNonNull(e.getGuild().getMember(u))).queue();
                    Objects.requireNonNull(e.getGuild().getTextChannelById("766082022141067285")).sendMessage(log).queue();
                    e.reply("Kicked user, **"+u.getAsTag()+"**, from the server!)");
                }
        ));
    }
}
