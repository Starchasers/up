import styled from 'styled-components'
import Shadow from './Shadow'
import Box from './Box'
import Text from './Text'
import StyledLink from './StyledLink'
import List from './List'
import ItemList from './ItemList'
import { px } from 'styled-components-spacing/dist/cjs'
import { breakpoint } from 'styled-components-breakpoint'

const MainBox = styled('article')`
  max-width: 768px;
  margin: 18vh auto 0;
  position: relative;
  ${px(3)};

  ${breakpoint('xs', 'sm')`
    margin: ${props => props.theme.spacing[3]} auto 0;
  `}
`

MainBox.Box = Box
MainBox.Shadow = Shadow
MainBox.Text = Text
MainBox.StyledLink = StyledLink
MainBox.List = List
MainBox.ItemList = ItemList

export default MainBox
