import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import com.sasha.reminecraft.api.RePlugin;
import com.sasha.reminecraft.api.event.ChatReceivedEvent;
import com.sasha.reminecraft.client.ReClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends RePlugin implements SimpleListener {

    // when sb sends u a msg with that command the bot knows to save that..
    // msg must be like .... wispers you: command McName msg
    public final String command = "!send";
    public final String wisp = "wispers";
    public ArrayList<MessageToSend> toSend = new ArrayList<>(); // TODO load all the msgs


    private ScheduledExecutorService executor;
    private Future<?> future;

    @Override
    public void onPluginInit() {
        this.getReMinecraft().EVENT_BUS.registerListener(this);

         executor = Executors.newScheduledThreadPool(2);
    }

    @Override
    public void onPluginEnable() {
        future = executor.scheduleAtFixedRate(() -> {
            testSbCame();
        }, 1L, 3L, TimeUnit.MINUTES); // TODO give good values
    }

    @Override
    public void onPluginDisable() {

        future.cancel(false);
    }

    @Override
    public void onPluginShutdown() {
        this.getReMinecraft().EVENT_BUS.deregisterListener(this);

        executor.shutdownNow();
    }


    public void testSbCame(){
        List<PlayerListEntry> players = ReClient.ReClientCache.INSTANCE.playerListEntries;
        GameProfile p;
        for (PlayerListEntry pp : players) {
            p = pp.getProfile();

            for(MessageToSend m : toSend){
                m.testToSend(p);
            }

        }


    }



    @SimpleEventHandler
    public void onEvent(ChatReceivedEvent e){
        String msg = e.getMessageText();
        if(!msg.contains(wisp))
            return;

        try {

            String author = msg.split(wisp)[0];

            msg = msg.split(wisp)[1];
            if(!msg.contains(command))
                return;

            msg = msg.split(command)[1];

            String receiver = msg.split(" ")[0];

            toSend.add(new MessageToSend(new GameProfile("", author), new GameProfile("", receiver), msg.substring(author.length())));
            // TODO smh also save that in the database || save it on shutdown or sth
        } catch (Exception ex){}
    }




    @Override
    public void registerCommands() {

    }

    @Override
    public void registerConfig() {

    }
}
