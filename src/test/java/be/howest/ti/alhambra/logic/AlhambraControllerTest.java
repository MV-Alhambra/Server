package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlhambraControllerTest {

    @Test
    void AlhambraCont() {
        AlhambraController c = new AlhambraController();

        //Test addLobby function
        assertEquals("001", c.addLobby("Test", 6, false));

        assertEquals(new Building(null, -1, null), c.getBuildings());

        List<Lobby> lobbylist = new ArrayList<>();
        Lobby l = new Lobby("001", "Test", 6, false);
        lobbylist.add(l);
        assertEquals(l, c.getGame("001"));

        assertEquals(lobbylist, c.getLobbies());
        c.joinLobby("001", "mitch");


        //assertEquals(l, c.getGame("001"));

        assertTrue(c.leaveLobby("001", "mitch"));

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    AlhambraController c1 = new AlhambraController();
                    c1.addLobby("Test", 6, false);
                    c1.leaveLobby("001", "test");
                }
        );

        assertEquals("player not in lobby", exception.getMessage());
        c.joinLobby("001", "test1");
        assertFalse(c.verifyToken("001", "no"));
        c.joinLobby("001", "test2");
        c.readyUp("001", "test1");
        c.readyUp("001", "test2");
        c.readyDown("001", "test1");
        c.readyUp("001", "test1");
        assertTrue(c.startLobby("001"));
        assertFalse(c.startLobby("001"));

        assertTrue(c.viewTown("001", "test1"));

        assertFalse(c.verifyToken("001", "no"));
        Game game = (Game) c.getGame("001");

        Building b1 = new Building(BuildingType.ARCADES, 5);
        game.findPlayer("test1").getBuildingsInHand().add(b1);
        assertTrue(c.giveDirk("001", "test1", b1));
        Coin[] arrayC = new Coin[1];
        arrayC[0] = (game.getBank().getBankCoins()[0]);
        assertEquals(game, c.takeCoins("001", "test1", arrayC));

        assertEquals(Location.getSurroundingLocations(new Location(0, 0)), c.getAvailableBuildLocations("001", "test2", Building.getDefaultWalls()));

        assertThrows(AlhambraGameRuleException.class, () -> c.buyBuilding("001", "test1", Currency.GREEN, arrayC));
        assertThrows(AlhambraEntityNotFoundException.class, () -> c.build("001", "test1", b1, new Location(0, 1)));
        assertThrows(AlhambraGameRuleException.class, () -> c.redesign("001", "test1", b1, new Location(0, 1)));
        assertThrows(AlhambraEntityNotFoundException.class, () -> c.redesign("001", "test2", b1, new Location(0, 1)));
    }

    @Test
    void currencies() {
        AlhambraController c = new AlhambraController();
        assertArrayEquals(Currency.values(), c.getCurrencies());
    }

    @Test
    void buildingTypes() {
        AlhambraController c = new AlhambraController();
        assertArrayEquals(BuildingType.values(), c.getBuildingTypes());
    }


}
