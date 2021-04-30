package br.com.qrdapio.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.qrdapio.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CardapioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cardapio.class);
        Cardapio cardapio1 = new Cardapio();
        cardapio1.setId(1L);
        Cardapio cardapio2 = new Cardapio();
        cardapio2.setId(cardapio1.getId());
        assertThat(cardapio1).isEqualTo(cardapio2);
        cardapio2.setId(2L);
        assertThat(cardapio1).isNotEqualTo(cardapio2);
        cardapio1.setId(null);
        assertThat(cardapio1).isNotEqualTo(cardapio2);
    }
}
