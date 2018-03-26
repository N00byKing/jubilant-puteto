#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "N00byKing/jubilant-puteto" ] && [ "$TRAVIS_JDK_VERSION" == "openjdk8" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone https://${GH_TOKEN}@github.com/N00byKing/jubilant-puteto-doc

  rm -rf ./jubilant-puteto-doc

  echo -e "Generating javadoc...\n"
  
  javadoc $(find . -name *.java) -d jubilant-puteto-doc/ 

  echo -e "Publishing javadoc...\n"

  git status
  git add *
  git commit -m "Latest javadoc for commit $TRAVIS_COMMIT auto-pushed to gh-pages"
  git push -f

  echo -e "Javadoc Updated.\n"
  
fi
