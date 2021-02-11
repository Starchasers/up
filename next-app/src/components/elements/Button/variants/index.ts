import StyledButton from '../StyledButton'
import ButtonText from '../ButtonText'

import PrimaryStyledButton from './primary/PrimaryStyledButton'
import PrimaryButtonText from './primary/PrimaryButtonText'
import UploadDashedButton from './uploadDashed/UploadDashedButton'
import UploadDashedButtonText from './uploadDashed/UploadDashedButtonText'
import TransparentStyledButton from './transparent/TransparentStyledButton'
import TransparentButtonText from './transparent/TransparentButtonText'

export type possibleVariants = 'primary' | 'uploadDashed' | 'transparent'

type TVariant = {
  Button: typeof StyledButton
  Text: typeof ButtonText
}

const variants: { [index in possibleVariants]: TVariant } = {
  primary: {
    Button: PrimaryStyledButton,
    Text: PrimaryButtonText
  },
  uploadDashed: {
    Button: UploadDashedButton,
    Text: UploadDashedButtonText
  },
  transparent: {
    Button: TransparentStyledButton,
    Text: TransparentButtonText
  }
}

export default variants
