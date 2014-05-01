A simple tool to generate ogml files which can be imported to RSS readers(tested on feedly).

## Usage ##

1. git clone https://github.com/freewind/TwBlogOpmlGenerator.git
2. ./sbt
3. wait for downloading dependencies for a while, until it shows `>`
4. > generate
  It will generate `tw-blogs.opml` based on the `blogs.txt` file. You can modify the `blogs.txt` to add new blogs.
6. Import the `tw-blogs.opml` which in on the root of the project into your favorite RSS reader