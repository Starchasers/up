import styled from 'styled-components'
import { ml, my } from 'styled-components-spacing/dist/cjs'

const ItemList = styled('li')`
  white-space: nowrap;
  position: relative;

  ${ml(4)};
  ${my(0)};

  &:not(:first-child):before {
    content: '○';
    position: absolute;
    left: calc(-${props => props.theme.spacing[3]} - 1px);
  }
`

export default ItemList
