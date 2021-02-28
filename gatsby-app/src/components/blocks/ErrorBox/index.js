import styled from 'styled-components'
import { pl } from 'styled-components-spacing/dist/cjs'

import Header from './Header'
import Back from './Back'
import Icon from './Icon'
import Text from './Text'

const ErrorBox = styled('div')`
  position: relative;
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  min-height: 300px;
  ${pl({md: 4})};
`

ErrorBox.Header = Header
ErrorBox.Back = Back
ErrorBox.Icon = Icon
ErrorBox.Text = Text

export default ErrorBox
