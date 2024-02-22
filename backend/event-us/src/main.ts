import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { SpelunkerModule } from 'nestjs-spelunker';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  
  const options = new DocumentBuilder()
    .setTitle('core-api')
    .setDescription('The core API description')
    .setVersion('3.0')
    .addTag('core-api')
    .build();
  const document = SwaggerModule.createDocument(app, options);
  SwaggerModule.setup('docs', app, document);

  // 1. Generate the tree as text
  const tree = SpelunkerModule.explore(app);
  const root = SpelunkerModule.graph(tree);
  const edges = SpelunkerModule.findGraphEdges(root);
  const mermaidEdges = edges.map(({ from, to }) => `${from.module.name}-->${to.module.name}`);
  console.log(`graph TD\n\t${mermaidEdges.join('\n\t')}`);

  await app.listen(3000);
}
bootstrap();
