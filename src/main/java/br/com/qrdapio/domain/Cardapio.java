package br.com.qrdapio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Cardapio.
 */
@Entity
@Table(name = "cardapio")
public class Cardapio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "cardapio")
    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    private Set<ItemCardapio> itemCardapios = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cardapios", "pedidos" }, allowSetters = true)
    private Restaurante restaurante;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cardapio id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Cardapio nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<ItemCardapio> getItemCardapios() {
        return this.itemCardapios;
    }

    public Cardapio itemCardapios(Set<ItemCardapio> itemCardapios) {
        this.setItemCardapios(itemCardapios);
        return this;
    }

    public Cardapio addItemCardapio(ItemCardapio itemCardapio) {
        this.itemCardapios.add(itemCardapio);
        itemCardapio.setCardapio(this);
        return this;
    }

    public Cardapio removeItemCardapio(ItemCardapio itemCardapio) {
        this.itemCardapios.remove(itemCardapio);
        itemCardapio.setCardapio(null);
        return this;
    }

    public void setItemCardapios(Set<ItemCardapio> itemCardapios) {
        if (this.itemCardapios != null) {
            this.itemCardapios.forEach(i -> i.setCardapio(null));
        }
        if (itemCardapios != null) {
            itemCardapios.forEach(i -> i.setCardapio(this));
        }
        this.itemCardapios = itemCardapios;
    }

    public Restaurante getRestaurante() {
        return this.restaurante;
    }

    public Cardapio restaurante(Restaurante restaurante) {
        this.setRestaurante(restaurante);
        return this;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cardapio)) {
            return false;
        }
        return id != null && id.equals(((Cardapio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cardapio{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
