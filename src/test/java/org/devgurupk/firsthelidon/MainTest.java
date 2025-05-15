
package org.devgurupk.firsthelidon;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import jakarta.json.JsonArray;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.client.Entity;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import io.helidon.metrics.api.MetricsFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@HelidonTest
class MainTest {

    @Inject
    private MetricRegistry registry;

    @Inject
    private WebTarget target;


    @Test
    void testHealth() {
        Response response = target
                .path("health")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    void testMicroprofileMetrics() {
        Message message = target.path("simple-greet/Joe")
                .request()
                .get(Message.class);

        assertThat(message.getMessage(), is("Hello Joe"));
        Counter counter = registry.counter("personalizedGets");
        double before = counter.getCount();

        message = target.path("simple-greet/Eric")
                .request()
                .get(Message.class);

        assertThat(message.getMessage(), is("Hello Eric"));
        double after = counter.getCount();
        assertEquals(1d, after - before, "Difference in personalized greeting counter between successive calls");
    }

    @AfterAll
    static void clear() {
        MetricsFactory.closeAll();
    }


    @Test
    void testPokemonTypes() {
        JsonArray types = target
                .path("type")
                .request()
                .get(JsonArray.class);
        assertThat(types.size(), is(18));
    }

    @Test
    void testPokemon() {
        assertThat(getPokemonCount(), is(6));

        Pokemon pokemon = target
                .path("pokemon/1")
                .request()
                .get(Pokemon.class);
        assertThat(pokemon.getName(), is("Bulbasaur"));

        pokemon = target
                .path("pokemon/name/Charmander")
                .request()
                .get(Pokemon.class);
        assertThat(pokemon.getType(), is(10));

        Response response = target
                .path("pokemon/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));

        Pokemon test = new Pokemon();
        test.setType(1);
        test.setId(100);
        test.setName("Test");
        response = target
                .path("pokemon")
                .request()
                .post(Entity.entity(test, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(204));
        assertThat(getPokemonCount(), is(7));

        response = target
                .path("pokemon/100")
                .request()
                .delete();
        assertThat(response.getStatus(), is(204));
        assertThat(getPokemonCount(), is(6));
    }

    private int getPokemonCount() {
        JsonArray pokemons = target
                .path("pokemon")
                .request()
                .get(JsonArray.class);
        return pokemons.size();
    }


    @Test
    void testGreet() {
        Message message = target
                .path("simple-greet")
                .request()
                .get(Message.class);
        assertThat(message.getMessage(), is("Hello World!"));
    }

}
