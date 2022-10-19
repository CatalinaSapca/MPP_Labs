
echo "Generating Java classes"
protoc -I=. --java_out=javaFiles --csharp_out=csharpFiles TSProtocol_v3.proto
