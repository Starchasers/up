import React from 'react'
import {
  SimpleForm,
  TextInput,
  Edit,
  required,
  RadioButtonGroupInput,
  PasswordInput
} from 'react-admin'

export default (props) => (
  <Edit
    {...props}
  >
    <SimpleForm onSubmit={(data) => console.log(data, props)}>
      <TextInput
        source='id'
        type='text'
        validate={required()}
        disabled
      />
      <TextInput
        source='username'
        type='text'
        validate={required()}
        disabled
      />
      <TextInput
        source='email'
        type='email'
      />
      <PasswordInput
        source='password'
        type='password'
      />
      <RadioButtonGroupInput source="roles" choices={[
        { id: 'USER', name: 'User' },
        { id: 'ADMIN', name: 'Admin' },
      ]}/>
    </SimpleForm>
  </Edit>
)


