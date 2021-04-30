package br.com.qrdapio.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.qrdapio.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemCardapioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemCardapio.class);
        ItemCardapio itemCardapio1 = new ItemCardapio();
        itemCardapio1.setId(1L);
        ItemCardapio itemCardapio2 = new ItemCardapio();
        itemCardapio2.setId(itemCardapio1.getId());
        assertThat(itemCardapio1).isEqualTo(itemCardapio2);
        itemCardapio2.setId(2L);
        assertThat(itemCardapio1).isNotEqualTo(itemCardapio2);
        itemCardapio1.setId(null);
        assertThat(itemCardapio1).isNotEqualTo(itemCardapio2);
    }
}
