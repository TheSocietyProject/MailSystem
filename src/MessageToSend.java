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


    public boolean testToSend(GameProfile p) {
        System.out.println("comparing" + p.getName() + " < > " + this.receiver.getName()); // TODO rly need UUID I guess
        if(!this.receiver.getName().equals(p.getName())) // TODO test wether it works cuz p doesnt have the UUID rn
            return false;
        String toSend = "/msg " + this.receiver.getName() + " " + this.author.getName() + " said: " + this.msg;
        ReMinecraft.INSTANCE.minecraftClient.getSession().send(new ClientChatPacket(toSend));


        return true;
    }
}
