import styled from 'styled-components'
import { pl } from 'styled-components-spacing/dist/cjs'
import TextBox from './TextBox'
import Text from './Text'
import Back from './Back'
import Icon from './Icon'

const AfterUploadBox = styled('div')`
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  min-height: 300px;
  ${pl({md: 4})};
  position: relative;
`

AfterUploadBox.TextBox = TextBox
AfterUploadBox.Text = Text
AfterUploadBox.Back = Back
AfterUploadBox.Icon = Icon

export default AfterUploadBox
