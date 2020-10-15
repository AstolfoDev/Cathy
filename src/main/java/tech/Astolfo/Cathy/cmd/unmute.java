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

public class unmute extends Command {

    public unmute() {
        super.name = "unmute";
        super.aliases = new String[]{"um","unshh"};
        super.arguments = "<@user>";
        super.help = "Un-mute a previously muted user";
        super.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        super.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    @Override
    protected void execute(CommandEvent e) {
        String[] args = e.getArgs().split("\\s+");

        if (args.length < 1) {
            e.replyError("**Invalid arguments!** Syntax: *"+e.getClient().getPrefix()+"unmute <@user> <reason>*");
            return;
        }

        List<User> users = e.getMessage().getMentionedUsers();
        if (users.size() != 1) {
            e.replyError("**Invalid mention count!** Syntax: *"+e.getClient().getPrefix()+"un <@user> <reason>*");
            return;
        }

        User u = users.get(0);

        if (!e.getMember().canInteract(Objects.requireNonNull(e.getGuild().getMember(u)))) {
            e.replyError("**Hey!** You you don't have permission to unmute that user");
            return;
        } else if (Objects.requireNonNull(e.getGuild().getMember(e.getSelfUser())).canInteract(Objects.requireNonNull(e.getGuild().getMember(u)))) {
            e.replyError("**Sorry!** I don't have permission to unmute that user");
            return;
        } else if (!Objects.requireNonNull(e.getGuild().getMember(u)).getRoles().contains(Objects.requireNonNull(e.getGuild().getRoleById("766329374601379891")))) {
            e.replyError("**Hey!** This user isn't muted");
            return;
        }

        MessageEmbed unmute_msg = new EmbedBuilder()
                .setAuthor("Team Astolfo", "https://astolfo.tech", e.getAuthor().getAvatarUrl())
                .setTitle("You've been un-muted!")
                .setThumbnail(u.getAvatarUrl())
                .setDescription("Hey "+u.getAsMention()+"!\nYou've been **un-muted** on the **Team Astolfo** Discord server, by **"+e.getAuthor().getAsTag()+"**")
                .setFooter("Team Astolfo", e.getGuild().getIconUrl())
                .setTimestamp(Instant.ofEpochMilli(System.currentTimeMillis()))
                .build();

        MessageEmbed log = new EmbedBuilder()
                .setAuthor(e.getAuthor().getAsTag(), null, e.getAuthor().getAvatarUrl())
                .setThumbnail(u.getAvatarUrl())
                .setTitle("Unmuted "+u.getAsTag()+" <a:dancin:763176000891912264>   ")
                .setDescription("ID: "+u.getId())
                .build();

        e.getGuild().removeRoleFromMember(u.getIdLong(), Objects.requireNonNull(e.getGuild().getRoleById("766329374601379891"))).queue();

        u.openPrivateChannel().queue((channel) -> channel.sendMessage(unmute_msg).queue());

        Objects.requireNonNull(e.getGuild().getTextChannelById("766082022141067285")).sendMessage(log).queue();
        e.reply("Un-muted user, **"+u.getAsTag()+"**!");
    }
}
