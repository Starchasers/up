import styled from 'styled-components'
import { ml, my } from 'styled-components-spacing/dist/cjs'

const ItemList = styled('li')`
  white-space: nowrap;
  position: relative;

  ${my(0)};

  &:not(:first-child) {
    ${ml(4)};
    &:before {
      content: '○';
      position: absolute;
      left: calc(-${props => props.theme.spacing[3]} - 1px);
    }
  }
`

export default ItemList
