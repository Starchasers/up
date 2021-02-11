import colors from './colors'

export type TPalletColor =
  'primary' |
  'secondary' |
  'third' |
  'background' |
  'text' |
  'lightText' |
  'white' |
  'black' |
  'grey'

const palette: {
  [index in TPalletColor]: string
} = {
  primary: colors.mako,
  secondary: colors.charade,
  third: colors.wedgewood,
  background: colors.shark,
  text: colors.white,
  lightText: colors.timberwolf,
  white: colors.white,
  black: colors.black,
  grey: colors.grey
}

export default palette
