import React from 'react'
import {
  SimpleForm,
  TextInput,
  Edit,
  required,
} from 'react-admin'

export default (props) => (
  <Edit
    {...props}
  >
    <SimpleForm>
      <TextInput
        source='email'
        type='text'
        validate={required()}
      />
      <TextInput
        source='password'
        type='password'
        validate={required()}
      />
    </SimpleForm>
  </Edit>
)


