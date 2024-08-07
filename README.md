# mkdocs-maven-plugin ![build status](https://shredzone.org/badge/mkdocs-maven-plugin.svg) ![maven central](https://shredzone.org/maven-central/org.shredzone.maven/mkdocs-maven-plugin/badge.svg)

This is a maven plugin that invokes [mkdocs](https://www.mkdocs.org/).

A recent version of `mkdocs` must be [installed](https://www.mkdocs.org/#installation) on your machine.

## Goals

This plugin offers three goals. All of them have these configuration options in common:

* `skip` (Property: `mkdocs.skip`): Skips mkdocs execution if set to `true`. Might be useful in some situations. Mkdocs is always skipped if no `mkdocs.yml` file can be found. This is useful if you use sub-modules, but only have documentation in the main module.

* `directory` (Property: `mkdocs.docs`): Directory of your documentation. By default, the `mkdocs.yml` and the mkdocs directory structure is expected here. Defaults to `${basedir}/src/doc`.

* `configFile` (Property: `mkdocs.mkfile`): The location of your `mkdocs.yml` file. By default it is expected in your `directory`.

* `mkdocsPath` (Property: `mkdocs.toolPath`): The location of the `mkdocs` tool that is to be executed. By default, it uses the first `mkdocs` command in your system path.

* `mkdocsCommand` (Property: `mkdocs.toolCommand`): Used as an alternative to `mkdocsPath`, it contains a list of `<arg>` options for the full `mkdocs` command and extra parameters. This can be used e.g. if mkdocs is to be run directly in Python. If set, `mkdocsPath` is ignored. 

* `environmentVars` (Property: `mkdocs.environment`): An optional map of environment variables to be set before running mkdocs.

All configuration parameters are optional.

### `mkdocs:build`

This goal builds from `src/doc` into `target/mkdocs`. You can change the behavior with the following configuration options:

* `theme` (Property: `mkdocs.theme`): The name of the theme to be used.

* `themeDirectory` (Property: `mkdocs.themeDirectory`): The path to a theme directory.

* `strict` (Property: `mkdocs.strict`): If set to `true`, mkdocs will abort the build on any warning. Defaults to `false`.

* `outputDirectory` (Property: `mkdocs.outputDirectory`): The target directory of your documentation. Defaults to `${project.build.directory}/mkdocs`.

* `clean` (Property: `mkdocs.clean`): If set to `true`, old files will be removed from the target directory before building. Defaults to `true`.

### `mkdocs:serve`

This goal serves your documentation on a local server, so you can view it on a browser while editing it. You can invoke `mvn mkdocs:serve` to start serving, and end it by pressing Ctrl-C.

* `host` (Property: `mkdocs.serve.host`): The host the server is listening at. Defaults to `localhost` or `127.0.0.1`.

* `port` (Property: `mkdocs.serve.port`): The port the server is listening on. Defaults to `8000`.

* `liveReload` (Property: `mkdocs.serve.liveReload`): If set to `true`, live reload mode is enabled. Defaults to `true`.

* `theme` (Property: `mkdocs.theme`): The name of the theme to be used.

* `themeDirectory` (Property: `mkdocs.themeDirectory`): The path to a theme directory.

* `strict` (Property: `mkdocs.strict`): If set to `true`, mkdocs will abort the build on any warning. Defaults to `false`.

### `mkdocs:gh-deploy`

This goal deploys your documentation to GitHub pages.

* `clean` (Property: `mkdocs.clean`): If set to `true`, old files will be removed from the target directory before building. Defaults to `true`.

* `message` (Property: `mkdocs.deploy.message`): Commit message.

* `remoteBranch` (Property: `mkdocs.deploy.remoteBranch`): The remote branch to commit to.

* `remoteName` (Property: `mkdocs.deploy.remoteName`): The remote name to commit to.

* `force` (Property: `mkdocs.deploy.force`): If set to `true`, push is forced.

## Dependency

Just add these lines to your `pom.xml` and insert the correct version number.

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.shredzone.maven</groupId>
      <artifactId>mkdocs-maven-plugin</artifactId>
      <version>..</version>
    </plugin>
  </plugins>
</build>
```

## Contribute

* Fork the [Source code at GitHub](https://github.com/shred/mkdocs-maven-plugin). Feel free to send pull requests.
* Found a bug? [File a bug report!](https://github.com/shred/mkdocs-maven-plugin/issues)

## License

_mkdocs-maven-plugin_ is open source software. The source code is distributed under the terms of [GNU General Public License Version 3](http://www.gnu.org/licenses/gpl-3.0.html).
