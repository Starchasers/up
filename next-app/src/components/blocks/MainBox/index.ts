import styled from 'styled-components'

import Content from './Content'
import Grid from './Grid'
import ShadowBox from './ShadowBox'

const MainBox = styled('div')`
  position: relative;
  max-width: ${props => props.theme.constants.containerSizes.md};
  margin: 0 auto;
  width: 100%;
`

MainBox.Content = Content
MainBox.Grid = Grid
MainBox.ShadowBox = ShadowBox

export default MainBox
