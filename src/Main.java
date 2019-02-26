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
    public final String wisp = " whispers: ";
    public ArrayList<MessageToSend> toSend = new ArrayList<>(); // TODO load all the msgs

    public int var_TestPlayersInitialDelay = 1000; //1 * 60 * 1000;
    public int var_TestPlayersPeriod = 1000; // 3 * 60 * 1000; // TODO give good values


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
        }, var_TestPlayersInitialDelay, var_TestPlayersPeriod, TimeUnit.MILLISECONDS);
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

            for(MessageToSend m : toSend) {
                if (m.testToSend(p)){
                    toSend.remove(m);
                    return;
                }
            }

        }


    }



    @SimpleEventHandler
    public void onEvent(ChatReceivedEvent e){
        String msg = e.getMessageText();
        if(!msg.contains(wisp))
            return;

        //
        try {
            // TODO anonymos
            String author = msg.split(wisp)[0];

            msg = msg.split(wisp)[1];


            if(!msg.contains(command))
                return;



            msg = msg.split(command + " ")[1];


            String receiver = msg.split(" ")[0];

            toSend.add(new MessageToSend(new GameProfile("", author), new GameProfile("", receiver), msg.substring(receiver.length() + 1)));// + 1 for the space
            // TODO smh also save that in the database || save it on shutdown or sth
            System.out.println("HERE: added in array");
        } catch (Exception ex){
            System.out.println("HERE: got ERROR: " + ex);
            // TODO whisper back
        }
    }




    @Override
    public void registerCommands() {

    }

    @Override
    public void registerConfig() {

    }
}
