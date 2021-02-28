import styled from 'styled-components'
import { pl } from 'styled-components-spacing/dist/cjs'

import Header from './Header'
import Back from './Back'
import Icon from './Icon'
import Text from './Text'

const NotFoundBox = styled('div')`
  position: relative;
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  min-height: 300px;
  ${pl({md: 4})};
`

NotFoundBox.Header = Header
NotFoundBox.Back = Back
NotFoundBox.Icon = Icon
NotFoundBox.Text = Text

export default NotFoundBox
