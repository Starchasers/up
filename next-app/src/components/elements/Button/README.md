# Button

Button is a reusable component that is used in every project.

There is no need to write every time a new logic for that element with its integration with next.js.
This template is allowing you to write your own style without writing logic for it.

To make a style for your desired button your should write your own `variant` or edit the existed one to
match your requirements.

## Styling
For shape and generally design: You should make a `variant`

For different colors: write your own `colorVariant`

## Variants

Your variant should contain keys like `button` or `text`. Where `button` is representing the button itself
and `text` is representing only text inside your button.
The `text` key is optional and only required if you want to change something with it.

You should avoid writing colors in it, instead use a color from prop like: `colors[props.color.unset]`
Where `unset` is one of your `colorVariants` possible states.

### Possible color variants
  * `unset` - default look
  * `hover` - look on hover
  * `active` - look on active
  * `focus` - look on focus
  * `disabled` - look when disabled

## Expected behavior for perfect design
Button should be able to fill the whole available area with itself without forcing any size.
To reduce width of your button you can use `ButtonContainer` that is a grid element where button is wrapped by
to reduce it from the desired side.
