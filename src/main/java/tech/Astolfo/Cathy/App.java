package tech.Astolfo.Cathy;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import tech.Astolfo.Cathy.cmd.*;

import javax.security.auth.login.LoginException;

public class App {
    public static void main(String[] args) throws LoginException {
        System.out.println("Starting...");

        JDA jda = new JDABuilder()
                .setToken(System.getenv("TOKEN"))
                .setActivity(Activity.listening("Plat"))
                .build();

        CommandClientBuilder builder = new CommandClientBuilder()
                .setPrefix("/")
                .setOwnerId("193044575500238849")
                .setActivity(Activity.listening("BigBrainPlat"))
                .setHelpWord("globglogabgalab")

                .addCommands(
                        new kick(),
                        new ban(),
                        new mute(),
                        new unmute(),
                        new purge()
                );

        CommandClient client = builder.build();

        jda.addEventListener(client);
    }
}
