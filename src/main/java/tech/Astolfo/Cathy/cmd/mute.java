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

public class mute extends Command {

    public mute() {
        super.name = "mute";
        super.aliases = new String[]{"m","shh"};
        super.arguments = "<@user> <reason>";
        super.help = "Mute an annoying member";
        super.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        super.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    @Override
    protected void execute(CommandEvent e) {
        String[] args = e.getArgs().split("\\s+");
        String reason = e.getArgs().replace(args[0]+" ", "");

        if (args.length < 2) {
            e.replyError("**Invalid arguments!** Syntax: *"+e.getClient().getPrefix()+"mute <@user> <reason>*");
            return;
        }

        List<User> users = e.getMessage().getMentionedUsers();
        if (users.size() != 1) {
            e.replyError("**Invalid mention count!** Syntax: *"+e.getClient().getPrefix()+"mute <@user> <reason>*");
            return;
        }

        User u = users.get(0);

        if (!e.getMember().canInteract(Objects.requireNonNull(e.getGuild().getMember(u)))) {
            e.replyError("**Hey!** You you don't have permission to mute that user");
            return;
        } else if (Objects.requireNonNull(e.getGuild().getMember(e.getSelfUser())).canInteract(Objects.requireNonNull(e.getGuild().getMember(u)))) {
            e.replyError("**Sorry!** I don't have permission to mute that user");
            return;
        }

        MessageEmbed mute_msg = new EmbedBuilder()
                .setAuthor("Team Astolfo", "https://astolfo.tech", e.getAuthor().getAvatarUrl())
                .setTitle("You've been muted!")
                .setThumbnail(u.getAvatarUrl())
                .setDescription("Hey "+u.getAsMention()+"!\nYou've been **muted** on the **Team Astolfo** Discord server.\nThis is so tragic omg! <:astolfoCry:763502101359099914>\n\nYou were muted by **"+e.getAuthor().getAsTag()+"** for:\n\""+reason+"\"")
                .setFooter("Team Astolfo", e.getGuild().getIconUrl())
                .setTimestamp(Instant.ofEpochMilli(System.currentTimeMillis()))
                .build();

        MessageEmbed log = new EmbedBuilder()
                .setAuthor(e.getAuthor().getAsTag(), null, e.getAuthor().getAvatarUrl())
                .setThumbnail(u.getAvatarUrl())
                .setTitle("Muted "+u.getAsTag()+" <a:eminem:763501558469754881>  ")
                .setDescription("ID: "+u.getId()+"\nReason: \""+reason+"\"")
                .build();

        e.getGuild().addRoleToMember(u.getIdLong(), Objects.requireNonNull(e.getGuild().getRoleById("766329374601379891"))).queue();

        u.openPrivateChannel().queue((channel) -> channel.sendMessage(mute_msg).queue());

        Objects.requireNonNull(e.getGuild().getTextChannelById("766082022141067285")).sendMessage(log).queue();
        e.reply("Muted user, **"+u.getAsTag()+"**!");
    }
}
