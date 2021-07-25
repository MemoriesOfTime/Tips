package tip.tasks;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

/**
 * @author SmallasWater
 * Create on 2021/7/25 12:40
 * Package tip.tasks
 */
public class FakeIPlayer implements IPlayer {


    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public String getName() {
        return "ServerPlayer";
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public void setBanned(boolean b) {

    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }

    @Override
    public void setWhitelisted(boolean b) {

    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public Server getServer() {
        return Server.getInstance();
    }

    @Override
    public Long getFirstPlayed() {
        return null;
    }

    @Override
    public Long getLastPlayed() {
        return null;
    }

    @Override
    public boolean hasPlayedBefore() {
        return false;
    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue) throws Exception {

    }

    @Override
    public List<MetadataValue> getMetadata(String s) throws Exception {
        return null;
    }

    @Override
    public boolean hasMetadata(String s) throws Exception {
        return false;
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) throws Exception {

    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean b) {

    }
}
