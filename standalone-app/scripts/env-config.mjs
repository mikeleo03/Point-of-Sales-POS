// scripts/env-config.mjs
import fs from 'fs';
import dotenv from 'dotenv';

dotenv.config();

const envConfig = {
  production: false,
  apiUrl: process.env.BASE_API_URL ?? '',
  apiKey: process.env.API_KEY ?? ''
};

fs.writeFileSync(
  'src/environment/environment.ts',
  `export const environment = ${JSON.stringify(envConfig, null, 2)};`
);