#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "N00byKing/jubilant-puteto" ] && [ "$TRAVIS_JDK_VERSION" == "openjdk8" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone https://${GH_TOKEN}@github.com/N00byKing/jubilant-puteto-doc

  echo -e "Generating javadoc...\n"

  javadoc -d jubilant-puteto-doc/ $(find . -name '*.java')

  echo -e "Publishing javadoc...\n"

  cd jubilant-puteto-doc
  git status
  git add *
  git commit -m "Latest javadoc for commit $TRAVIS_COMMIT auto-pushed to N00byKing/jubilant-puteto-doc"
  git push -f

  echo -e "Javadoc Updated.\n"
  
  cd ..
  
  echo -e "Updating Submodule Link.\n"

  git submodule init
  cd doc
  git pull
  cd ..
  git commit -m 'Latest javadoc linked'
  git push
fi
