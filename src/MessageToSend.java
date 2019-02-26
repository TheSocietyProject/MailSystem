import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.sasha.reminecraft.ReMinecraft;

public class MessageToSend {


    public GameProfile author, receiver;
    public String msg;

    public MessageToSend(GameProfile author, GameProfile receiver, String msg){
        this.author = author;
        this.receiver = receiver;
// TODO smh add UUID to GameProfile
        this.msg = msg;
    }


    public void testToSend(GameProfile p) {
        if(!this.receiver.equals(p)) // TODO test wether it works cuz p doesnt have the UUID rn
            return;
        ReMinecraft.INSTANCE.minecraftClient.getSession().send(new ClientChatPacket("/msg " + this.receiver + " " + this.author + " said: " + this.msg));




    }
}
