import iut.gon.hexagonal_coordinates.Direction;
import iut.gon.hexagonal_coordinates.Direction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {
    @Test
    void testOpposite(){
        assertEquals(Direction.S,  Direction.N.opposite());
        assertEquals(Direction.N,  Direction.S.opposite());
        assertEquals(Direction.SO, Direction.NE.opposite());
        assertEquals(Direction.NE, Direction.SO.opposite());
        assertEquals(Direction.SE, Direction.NO.opposite());
        assertEquals(Direction.NO, Direction.SE.opposite());
        assertEquals(Direction.O,  Direction.E.opposite());
        assertEquals(Direction.E,  Direction.O.opposite());
    }

    @Test
    void testOppositeEstSymetrique(){
        for( Direction d : Direction.values())
            assertEquals(d, d.opposite().opposite());
    }

}
