import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/restaurante">
      <Translate contentKey="global.menu.entities.restaurante" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/cardapio">
      <Translate contentKey="global.menu.entities.cardapio" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/item-cardapio">
      <Translate contentKey="global.menu.entities.itemCardapio" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/pedido">
      <Translate contentKey="global.menu.entities.pedido" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/item-pedido">
      <Translate contentKey="global.menu.entities.itemPedido" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
