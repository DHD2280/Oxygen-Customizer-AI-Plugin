#!/bin/bash

NEWVERCODE=$(($(cat app/build.gradle.kts | grep versionCode | tr -s ' ' | cut -d " " -f 4 | tr -d '\r')+1))
NEWVERNAME=${GITHUB_REF_NAME/v/}

sed -i 's/versionCode.*/versionCode = '$NEWVERCODE'/' app/build.gradle.kts
sed -i 's/versionName =.*/versionName = "'$NEWVERNAME'"/' app/build.gradle.kts

sed -i 's/"version":.*/"version": "'$NEWVERNAME'",/' latestVersion.json
sed -i 's/"versionCode":.*/"versionCode": '$NEWVERCODE',/' latestVersion.json
sed -i 's/"apkUrl":.*/"apkUrl": "https:\/\/github.com\/DHD2280\/Oxygen-Customizer-AI-Plugin\/releases\/download\/'$GITHUB_REF_NAME'\/OxygenCustomizerAIPlugin.apk"/' latestVersion.json

# module changelog
echo "**$NEWVERNAME**  " > newChangeLog.md
cat changeLog.md >> newChangeLog.md
echo "  " >> newChangeLog.md
cat Changelog.md >> newChangeLog.md
mv  newChangeLog.md Changelog.md

# release message
echo "**$NEWVERNAME**  " > release.msg
echo "  " >> release.msg
echo "*Changelog:*  " >> release.msg
cat changeLog.md >> release.msg
echo 'RMessage<<EOF' >> $GITHUB_ENV
cat release.msg >> $GITHUB_ENV
echo 'EOF' >> $GITHUB_ENV