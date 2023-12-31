import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-phone-hint' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const PhoneHint = NativeModules.PhoneHint
  ? NativeModules.PhoneHint
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export async function initPhoneNumberHint(): Promise<string> {
  return PhoneHint.initPhoneNumberHint();
}
