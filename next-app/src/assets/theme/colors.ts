export type TColor =
  'transparent' |
  'white' |
  'timberwolf' |
  'grey' |
  'wedgewood' |
  'mako' |
  'charade' |
  'shark' |
  'black'

/**
 List of possible colors in your app.
 This is used for better color management and suggestions from your *Editor*.
 You should *avoid* using it directly if there is a color in your palette that represents it; like `primary` or `secondary`.

 Every index must have a comment: `@color #ffffff rgb(255,255,255)` with a hex value that represents a color.
 It helps to identify the desired color from this list.
 **/

const colors: {
  [index in TColor]: string
} = {
  // @color transparent
  transparent: 'transparent',
  // @color #ffffff rgb(255,255,255)
  white: '#FFFFFF',
  // @color #DAD7D2 rgb(218,215,210)
  timberwolf: '#DAD7D2',
  // @color #808080	rgb(128,128,128)
  grey: '#808080',
  // @color #5678A6 rgb(86,120,166)
  wedgewood: '#5678A6',
  // @color #434755 rgb(67,71,85)
  mako: '#434755',
  // @color #1f2229 rgb(41,41,55)
  charade: '#1F2229',
  // @color #1A1B1C rgb(26,27,28)
  shark: '#1A1B1C',
  // @color #000000 rgb(0,0,0)
  black: '#000000'
}

export default colors
