package yaboichips.rogue_planets.common.nbt.parties;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PartyData extends SavedData {
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);

    public static final SavedDataType<PartyData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath("rogueplanets", "party_data"),
            PartyData::new,
            partyDataCodec(),
            DataFixTypes.LEVEL
    );

    private final Map<UUID, Party> playerPartyMap = new HashMap<>();
    private final Map<UUID, Party> parties = new HashMap<>();

    public PartyData() {
    }

    private PartyData(List<Party> loadedParties) {
        for (Party party : loadedParties) {
            this.parties.put(party.leader, party);
            for (UUID member : party.members) {
                this.playerPartyMap.put(member, party);
            }
        }
    }

    private List<Party> getPartiesForSaving() {
        return List.copyOf(new HashSet<>(parties.values()));
    }

    private static Codec<PartyData> partyDataCodec() {
        return Party.CODEC.listOf().xmap(PartyData::new, PartyData::getPartiesForSaving);
    }

    public static PartyData get(ServerLevel level) {
        return level.getServer().getDataStorage().computeIfAbsent(TYPE);
    }

    // === API ===

    public boolean isInParty(UUID player) {
        return playerPartyMap.containsKey(player);
    }

    public boolean isLeader(UUID player) {
        return playerPartyMap.containsKey(player) && playerPartyMap.get(player).leader.equals(player);
    }

    public Party getParty(UUID player) {
        return playerPartyMap.get(player);
    }

    public void createParty(UUID leader) {
        if (isInParty(leader)) return;
        Party party = new Party(leader);
        parties.put(leader, party);
        playerPartyMap.put(leader, party);
        setDirty();
    }

    public void addPlayerToParty(UUID leader, UUID player) {
        Party party = parties.get(leader);
        if (party != null && !isInParty(player)) {
            party.members.add(player);
            playerPartyMap.put(player, party);
            setDirty();
        }
    }

    public void removePlayerFromParty(UUID player) {
        Party party = playerPartyMap.get(player);
        if (party != null) {
            party.members.remove(player);
            playerPartyMap.remove(player);
            if (party.members.isEmpty()) {
                parties.remove(party.leader);
            }
            setDirty();
        }
    }

    public void disbandParty(UUID leader) {
        Party party = parties.remove(leader);
        if (party != null) {
            for (UUID member : new HashSet<>(party.members)) {
                playerPartyMap.remove(member);
            }
            setDirty();
        }
    }

    // === Inner Party Class ===
    public static class Party {
        public static final Codec<Party> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                UUID_CODEC.fieldOf("leader").forGetter(p -> p.leader),
                UUID_CODEC.listOf().xmap((List<UUID> list) -> (Set<UUID>) new HashSet<>(list), (Set<UUID> set) -> List.copyOf(set)).fieldOf("members").forGetter(p -> p.members),
                Codec.BOOL.fieldOf("leaderInDimension").forGetter(Party::isLeaderInDimension)
        ).apply(instance, Party::fromParts));

        public final UUID leader;
        public final Set<UUID> members = Sets.newHashSet();
        private boolean leaderInDimension = false;

        public Party(UUID leader) {
            this.leader = leader;
            members.add(leader);
        }

        private static Party fromParts(UUID leader, Set<UUID> members, boolean leaderInDimension) {
            Party party = new Party(leader);
            party.members.clear();
            party.members.addAll(members);
            party.leaderInDimension = leaderInDimension;
            return party;
        }

        public boolean isLeaderInDimension() {
            return leaderInDimension;
        }

        public void setLeaderInDimension(boolean inDimension) {
            this.leaderInDimension = inDimension;
        }

        public void toggleLeaderInDimension() {
            this.leaderInDimension = !this.leaderInDimension;
        }
    }
}
