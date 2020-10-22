import React from 'react'
import {
  SimpleForm,
  TextInput,
  Create,
  required,
  RadioButtonGroupInput,
  PasswordInput,
  email
} from 'react-admin'

export default (props) => (
  <Create
    {...props}
    undoable='false'
  >
    <SimpleForm redirect='list'>
      <TextInput
        source='username'
        type='text'
        validate={required()}
      />
      <TextInput
        source='email'
        type='email'
        validate={[required(), email()]}
      />
      <PasswordInput
        source='password'
        type='password'
        validate={required()}
      />
      <RadioButtonGroupInput
        source='role'
        defaultValue='USER'
        choices={[
          { id: 'USER', name: 'User' },
          { id: 'ADMIN', name: 'Admin' },
        ]}
        validate={required()}
      />
    </SimpleForm>
  </Create>
)


