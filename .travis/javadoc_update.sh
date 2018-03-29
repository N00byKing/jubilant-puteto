#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "N00byKing/jubilant-puteto" ] && [ "$TRAVIS_JDK_VERSION" == "openjdk8" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone https://${GH_TOKEN}@github.com/N00byKing/jubilant-puteto-doc

  echo -e "\n Generating javadoc...\n"

  javadoc -d jubilant-puteto-doc/ $(find . -name '*.java')

  echo -e "\n Publishing javadoc...\n"

  cd jubilant-puteto-doc
  git add *
  git commit -m "Latest javadoc for commit $TRAVIS_COMMIT auto-pushed to N00byKing/jubilant-puteto-doc"
  git push -f

  echo -e "\n Javadoc Updated.\n"
  
  cd ..
  
  echo -e "\n Updating Submodule Link for Github Pages...\n"

  git clone https://${GH_TOKEN}@github.com/N00byKing/n00byking.github.io
  cd n00byking.github.io
  git submodule update --init --recursive
  git submodule update --remote --merge
  git add docs/jubilant-puteto/
  git commit -m 'Latest javadoc for N00byKing/jubilant-puteto ($TRAVIS_COMMIT) linked to Github Pages'
  git push
  
fi
